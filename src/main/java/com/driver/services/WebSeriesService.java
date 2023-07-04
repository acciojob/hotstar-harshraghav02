package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        ProductionHouse productionHouse = optionalProductionHouse.get();

        for(WebSeries webSeries : webSeriesRepository.findAll()){
            if(webSeries.getSeriesName().equals(webSeriesEntryDto.getSeriesName())){
                throw new Exception("Series is already present");
            }
        }

        WebSeries webSeries = new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);

        productionHouse.getWebSeriesList().add(webSeries);
        int totalRating = 0;
        int countOfWebSeries = productionHouse.getWebSeriesList().size();

        for(WebSeries webSeries1 : productionHouse.getWebSeriesList()){
            totalRating += webSeries1.getRating();
        }
        int newRating = totalRating/countOfWebSeries;
        productionHouse.setRatings(newRating);

        productionHouseRepository.save(productionHouse);
        //WebSeries savedWebseries = webSeriesRepository.save(webSeries);
        return webSeries.getId();
    }

}
