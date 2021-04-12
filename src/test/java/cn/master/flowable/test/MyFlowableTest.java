package cn.master.flowable.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cn.master.flowable.FlowableDevApplication;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/31 17:11
 */

//@EnsureCleanDb(dropDb = false)
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest(classes = FlowableDevApplication.class)
public @interface MyFlowableTest {

}
