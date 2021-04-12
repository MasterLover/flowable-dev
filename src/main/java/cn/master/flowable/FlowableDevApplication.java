package cn.master.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(proxyBeanMethods = false)
public class FlowableDevApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlowableDevApplication.class, args);
  }

}
