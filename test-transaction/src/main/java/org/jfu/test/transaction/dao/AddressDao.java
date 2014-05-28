package org.jfu.test.transaction.dao;

import org.jfu.test.transaction.po.Address;

public interface AddressDao {

    Address select(Integer id);

    int insert(Address address);

    int update(Address address);
}
