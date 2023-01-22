package com.example.userservice.service;

import com.example.userservice.feignClient.OrderServiceClient;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.management.modelmbean.ModelMBean;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    RestTemplate restTemplate;
    private UserRepository userRepository;

    private  BCryptPasswordEncoder passwordEncoder;

    private OrderServiceClient orderServiceClient;

    CircuitBreakerFactory circuitBreakerFactory;
    @Autowired
    public UserServiceImpl(OrderServiceClient orderServiceClient,RestTemplate restTemplate,UserRepository userRepository,BCryptPasswordEncoder passwordEncoder, CircuitBreakerFactory circuitBreakerFactory){
        this.orderServiceClient =orderServiceClient;
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       UserEntity userEntity = userRepository.findByEmail(username);

       if( userEntity == null){
           throw new UsernameNotFoundException(username);
       }

        return new User(userEntity.getUserId(),userEntity.getEncryptedPwd(),
                true,true,true,true,
                new ArrayList<>());
    }
    @Override
    public RequestUser createuser(RequestUser user) {

        user.setUserId(UUID.randomUUID().toString());


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(user, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(user.getPassword()));
        userRepository.save(userEntity);
        return user;
    }

    @Override
    public ResponseUser getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseUser responseUser = mapper.map(userEntity, ResponseUser.class);

        //feign client
        log.info("Before call orders microservice");
        List<ResponseOrder> responseOrders =  orderServiceClient.getOrders(userId);
        log.info("After called orders microservice");

        log.info("Before call orders microservice");
        //circuitbreaker-resilience4j
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> ordersList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
               throwable -> new ArrayList<>());
        log.info("After called orders microservice");



        responseUser.setOrders(ordersList);


        return responseUser;
    }

    @Override
    public List<ResponseUser> getUserByAll() {

        Iterable<UserEntity> userEntity = userRepository.findAll();
        List<ResponseUser> result = new ArrayList<>();
        userEntity.forEach(v->{
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });

        return result;
    }

    @Override
    public RequestUser getUserDetailsByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);
        RequestUser  requestUser = new ModelMapper().map(userEntity,RequestUser.class);

        return requestUser;
    }


}
