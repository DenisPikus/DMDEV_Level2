package com.dpdev.util;

import com.dpdev.entity.Brand;
import com.dpdev.entity.Cart;
import com.dpdev.entity.Order;
import com.dpdev.entity.OrderItems;
import com.dpdev.entity.Product;
import com.dpdev.entity.Stock;
import com.dpdev.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {
    public static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Brand.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(Stock.class);
        configuration.addAnnotatedClass(Cart.class);
        configuration.addAnnotatedClass(OrderItems.class);
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
