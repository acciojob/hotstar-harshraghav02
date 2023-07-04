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

    public int plan(int noOfScreen,SubscriptionType subType){
        int plan;
        if(subType.equals(SubscriptionType.BASIC)){
            plan=500+200*noOfScreen;
        }
        if(subType.equals(SubscriptionType.PRO)){
            plan=800+250*noOfScreen;
        }   else{
            plan=1000+350*noOfScreen;
        }

        return plan;
    }

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){
//        For Basic Plan : 500 + 200noOfScreensSubscribed
//        For PRO Plan : 800 + 250noOfScreensSubscribed
//        For ELITE Plan : 1000 + 350*noOfScreensSubscribed

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();

        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        subscription.setUser(user);


        int noOfscreen=subscriptionEntryDto.getNoOfScreensRequired();
        SubscriptionType subType=subscriptionEntryDto.getSubscriptionType();

        Integer plan=plan(noOfscreen,subType);



        subscription.setTotalAmountPaid(plan);
        user.setSubscription(subscription);

        userRepository.save(user);


        return plan;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();

        if(subscription.getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }

        int currPlan=subscription.getTotalAmountPaid();

        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            int plan = plan(subscription.getNoOfScreensSubscribed(),SubscriptionType.PRO);
            subscription.setTotalAmountPaid(plan);
            subscription.setSubscriptionType(SubscriptionType.PRO);
        } else  {
            int plan = plan(subscription.getNoOfScreensSubscribed(),SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(plan);
            subscription.setSubscriptionType(SubscriptionType.ELITE);
        }

        user.setSubscription(subscription);


        return subscription.getTotalAmountPaid()-currPlan;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        List<Subscription> allSub=subscriptionRepository.findAll();
        int totalRevenue=0;

        for(Subscription s:allSub){
            totalRevenue+=s.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}