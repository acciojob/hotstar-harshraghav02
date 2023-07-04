package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setStartSubscriptionDate(new Date());
        subscription.setUser(user);



        if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.BASIC){
            subscription.setTotalAmountPaid(500+200*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else if(subscriptionEntryDto.getSubscriptionType() == SubscriptionType.PRO){
            subscription.setTotalAmountPaid(800+250*subscriptionEntryDto.getNoOfScreensRequired());
        }
        else{
            subscription.setTotalAmountPaid(1000+350*subscriptionEntryDto.getNoOfScreensRequired());
        }

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        user.setSubscription(savedSubscription);
        userRepository.save(user);
        return savedSubscription.getTotalAmountPaid();
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user = userRepository.findById(userId).get();



        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)){
            // upgrade to PRO

            int presentAmount = user.getSubscription().getTotalAmountPaid();
            int newAmount = 800 + 250*user.getSubscription().getNoOfScreensSubscribed();

            Subscription subscription = user.getSubscription();
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setStartSubscriptionDate(new Date());
            subscription.setTotalAmountPaid(newAmount);
            subscriptionRepository.save(subscription);
            return newAmount-presentAmount;
        }
        else if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)){
            // upgrade to ELITE

            int presentAmount = user.getSubscription().getTotalAmountPaid();
            int newAmount = 1000 + 350*user.getSubscription().getNoOfScreensSubscribed();

            Subscription subscription = user.getSubscription();
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setStartSubscriptionDate(new Date());
            subscription.setTotalAmountPaid(newAmount);
            subscriptionRepository.save(subscription);
            return newAmount-presentAmount;
        }
        else{
            throw new Exception("Already the best Subscription");
        }

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int totalRevenue = 0;

        for(Subscription subscription : subscriptionRepository.findAll()){
            totalRevenue += subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
