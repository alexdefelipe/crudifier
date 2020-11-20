package com.jobosk.crudifier.resolver;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.UUID;

public abstract class GenericIdResolver<Entity> implements ObjectIdResolver {

    @Autowired
    private JpaRepository<Entity, UUID> repository;

    public GenericIdResolver() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void bindItem(final ObjectIdGenerator.IdKey id, final Object pojo) {
    }

    @Override
    public Object resolveId(final ObjectIdGenerator.IdKey id) {
        return repository.findById((UUID) id.key).orElseThrow(
                () -> new RuntimeException("Found reference to non-existen entity during object serialization")
        );
    }

    @Override
    public ObjectIdResolver newForDeserialization(final Object context) {
        return this;
    }

    @Override
    public boolean canUseFor(final ObjectIdResolver resolverType) {
        return false;
    }
}
