package com.whatsapp.images.configuration;

import com.whatsapp.images.lib.ImageOperations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {
      @Bean
      public ImageOperations imageOperations(){
            return ImageOperations.getCommonsIoInstance();
      }
      
}
