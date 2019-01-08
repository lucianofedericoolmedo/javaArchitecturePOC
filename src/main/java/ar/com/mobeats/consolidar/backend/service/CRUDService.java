package ar.com.mobeats.consolidar.backend.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import ar.com.mobeats.consolidar.backend.pagination.JPAPaginationRepository;
import ar.com.mobeats.consolidar.backend.pagination.PageRequest;
import ar.com.mobeats.consolidar.backend.pagination.PageResponse;

@Transactional
public abstract class CRUDService<T> {

    protected abstract JPAPaginationRepository<T, String> getRepository();

    protected abstract Logger getLogger();

    protected abstract Class<T> getModelClass();

    protected String getTypeName() {
        return this.getModelClass().getName();
    }

    public T create(T model) {
        model = this.getRepository().save(model);
        this.getLogger().info(this.getTypeName() + " added: " + model.toString());
        return model;
    }

    public T update(T model) {
        model = this.getRepository().save(model);
        this.getLogger().info(this.getTypeName() + " updated: " + model.toString());
        return model;
    }

    public void delete(T model) {
        this.getRepository().delete(model);
        this.getLogger().info(this.getTypeName() + " deleted: " + model.toString());
    }

    public T get(String id) {
        T model = this.getRepository().findOne(id);
        if (model == null) {
            this.getLogger().info("No " + this.getTypeName() + " found id " + id);
        } else {
            this.getLogger().info(this.getTypeName() + " found id " + id);// + " : " + model.toString());
        }
        return model;
    }

    protected PageResponse<T> find(Specification<T> sp, PageRequest pageRequest) {
        Page<T> response = this.getRepository().findAll(sp, pageRequest.convert());

        if (!response.hasContent()) {
            this.getLogger().info(this.getTypeName() + " not found");
        } else {
            this.getLogger().info(this.getTypeName() + " found count " + response.getSize());
        }

        return new PageResponse<T>(response.getContent(), response.getNumber(), response.getSize(),
            response.getTotalElements());
    }
    
    protected PageResponse<T> find(Specification<T> sp, org.springframework.data.domain.PageRequest pageRequest) {
        Page<T> response = this.getRepository().findAll(sp, pageRequest);

        if (!response.hasContent()) {
            this.getLogger().info(this.getTypeName() + " not found");
        } else {
            this.getLogger().info(this.getTypeName() + " found count " + response.getSize());
        }

        return new PageResponse<T>(response.getContent(), response.getNumber(), response.getSize(),
                response.getTotalElements());
    }

    public List<T> findAll() {
        List<T> model = this.getRepository().findAll();
        if (model == null || model.isEmpty()) {
            this.getLogger().info(this.getTypeName() + " not found");
        } else {
            this.getLogger().info(this.getTypeName() + " found count " + model.size());
        }
        return model;
    }
    
}
