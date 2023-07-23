package com.dpdev.entity;

public interface BaseEntity<K> {

    void setId(K id);

    K getId();
}
