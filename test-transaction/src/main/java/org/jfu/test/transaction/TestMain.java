package org.jfu.test.transaction;

import org.jfu.test.transaction.dao.AddressDao;
import org.jfu.test.transaction.dao.UserDao;
import org.jfu.test.transaction.po.Address;
import org.jfu.test.transaction.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestMain {
    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                "org/jfu/test/transaction/applicationContext.xml");

        TestMain testMain = applicationContext.getBean(TestMain.class);

        testMain.test();

        applicationContext.close();

    }

    @Transactional(propagation=Propagation.REQUIRED)
    public void test() {
        Address address = new Address();
        address.setDetail("测试");
        address.setPostcode(100023);
        addressDao.insert(address);

        User user = new User();
        user.setAddressId(address.getId());
        user.setEmail("jefffu@tianji.com");
        user.setName("Jeff Fu");
        userDao.insert(user);

        throw new NullPointerException("test");
    }

}
