package com.weatherforecast.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateEveryFourHours {


    @Scheduled(cron = "0 0 */4 * * *")
    public void updateDataBaseEveryFourHours() {



    }
}
