package com.intern.wlacheta.testapp.mappers;

public interface IMappersUtils<T, U> {
    T fromModelToDB(U objectToMap);
    U fromDBToModel(T objectToMap);
}
