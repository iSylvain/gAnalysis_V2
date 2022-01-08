package com.givaudan.galaxy.dao;

import com.givaudan.galaxy.model.core.CoreData;
import com.givaudan.galaxy.model.core.Product;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;

@Stateless
public class ProductDAO {

    private static final Logger logger = Logger.getLogger(ProductDAO.class.getName());

    private final String module = "gAnalysis";
    
    @PersistenceContext
    private EntityManager entityManager;

    public Product findById(Long id) {

        try {
            logger.info("findById called");
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> object = query.from(Product.class);
            query.where(cb.equal(object.get("id"), id));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public Product findByIdJoinPicture(Long id) {
//        logger.info("findByIdJoinPicture called");
        
        try {  
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> object = query.from(Product.class);
            Fetch<Product, CoreData> picture = object.fetch("picture", JoinType.LEFT);
            query.where(cb.equal(object.get("id"), id));
            Product product = entityManager.createQuery(query).getSingleResult();

            return product;

        } catch (NoResultException | NonUniqueResultException nre) {
            logger.log(Level.SEVERE, "Error findById : {0}", nre.getMessage());
            return null;
        }
    }

    public List<Product> findAll() {
//        logger.info("findAll called");
        
        try {            
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> object = query.from(Product.class);
            query.select(object);
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException | IllegalArgumentException nre) {
            logger.log(Level.SEVERE, "Error findAll : {0}", nre.getMessage());
            return null;
        }
    }

    public List<Product> searchProductWithNameOrCode(String searchString) {
//        logger.info("searchProductWithNameOrCode called");
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> object = query.from(Product.class);

            Path<String> pathName_en = object.get("name_en");
            Path<String> pathName_fr = object.get("name_fr");
            Path<String> pathName_es = object.get("name_es");
            Path<String> pathName_de = object.get("name_de");
            Path<String> pathCode = object.get("code");

            Predicate predicate = cb.or(cb.like(pathName_en, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_fr, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_es, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_de, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathCode, "%" + searchString.toLowerCase() + "%"));

            query.where(predicate);

            query.select(object);
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException | IllegalArgumentException nre) {
            logger.log(Level.SEVERE, "Error searchProductWithNameOrCode : {0}", nre.getMessage());
            return null;
        }
    }

    public List<Product> searchProductWithNameOrCodeInModule(String searchString) {
//        logger.info("searchProductWithNameOrCodeInModule called");
        
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> object = query.from(Product.class);

            Path<String> pathName_en = object.get("name_en");
            Path<String> pathName_fr = object.get("name_fr");
            Path<String> pathName_es = object.get("name_es");
            Path<String> pathName_de = object.get("name_de");
            Path<String> pathCode = object.get("code");
            Path<Boolean> pathModule = object.get(module);

            Predicate predicate = cb.or(cb.like(pathName_en, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_fr, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_es, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathName_de, "%" + searchString.toLowerCase() + "%"),
                    cb.like(pathCode, "%" + searchString.toLowerCase() + "%"));

            query.where(cb.isTrue(pathModule), predicate);

            query.select(object);
            return entityManager.createQuery(query).getResultList();
        } catch (NoResultException | IllegalArgumentException nre) {
            logger.log(Level.SEVERE, "Error searchProductWithNameOrCode : {0}", nre.getMessage());
            return null;
        }
    }

    public int count(Map<String, Object> filters) {
//        logger.log(Level.INFO, "count called.");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product);

        //filter
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, Object> filter : filters.entrySet()) {

            logger.log(Level.INFO, "filterCondition with key : {0} and value : {1}", new Object[]{filter.getKey(), filter.getValue()});

            if (!filter.getValue().equals("")) {
                //try as string using like
                Path<String> pathFilter = getStringPath(filter.getKey(), product);
                if (pathFilter != null) {
                    filterCondition = cb.and(filterCondition, cb.like(pathFilter, "%" + filter.getValue() + "%"));
                } else {
                    //try as non-string using equal
                    Path<?> pathFilterNonString = getPath(filter.getKey(), product);
                    filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
                }
            }
        }
        query.where(filterCondition);

        int result = entityManager.createQuery(query).getResultList().size();
        logger.log(Level.INFO, "result: {0}", result);
        return result;
    }

    public List<Product> findProducts(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//        logger.log(Level.INFO, "findProducts called with first : {0} and page size : {1}", new Object[]{first, pageSize});

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product);

        //sort
        logger.log(Level.INFO, "findProducts called with sort : {0} and sortOrder : {1}", new Object[]{sortField, sortOrder.name()});
        Path<?> path = getPath(sortField, product);
        if (sortOrder == null) {
            //just don't sort
        } else if (sortOrder.equals(SortOrder.ASCENDING)) {
            query.orderBy(cb.asc(path));
        } else if (sortOrder.equals(SortOrder.DESCENDING)) {
            query.orderBy(cb.desc(path));
        } else if (sortOrder.equals(SortOrder.UNSORTED)) {
            //just don't sort
        } else {
            //just don't sort
        }

        //filter
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, Object> filter : filters.entrySet()) {

            logger.log(Level.INFO, "filterCondition with key : {0} and value : {1}", new Object[]{filter.getKey(), filter.getValue()});

            if (!filter.getValue().equals("")) {
                //try as string using like
                Path<String> pathFilter = getStringPath(filter.getKey(), product);
                if (pathFilter != null) {
                    filterCondition = cb.and(filterCondition, cb.like(pathFilter, "%" + filter.getValue() + "%"));
                } else {
                    //try as non-string using equal
                    Path<?> pathFilterNonString = getPath(filter.getKey(), product);
                    filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
                }
            }
        }
        query.where(filterCondition);

        TypedQuery<Product> tq = entityManager.createQuery(query);
        
        //pagination
        if (pageSize >= 0) {
            tq.setMaxResults(pageSize);
        }
        if (first >= 0) {
            tq.setFirstResult(first);
        }

        return tq.getResultList();
    }

    public int countProductInModule(Map<String, Object> filters, boolean ifModuleExist) {
//        logger.log(Level.INFO, "count called.");

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product);

        Path<Boolean> pathModule = product.get(module);

        //filter
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, Object> filter : filters.entrySet()) {

//            logger.log(Level.INFO, "filterCondition with key : {0} and value : {1}", new Object[]{filter.getKey(), filter.getValue()});
            if (!filter.getValue().equals("")) {
                //try as string using like
                Path<String> pathFilter = getStringPath(filter.getKey(), product);
                if (pathFilter != null) {
                    filterCondition = cb.and(filterCondition, cb.like(pathFilter, "%" + filter.getValue() + "%"));
                } else {
                    //try as non-string using equal
                    Path<?> pathFilterNonString = getPath(filter.getKey(), product);
                    filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
                }
            }
        }

        Predicate predicateModule;
        if (ifModuleExist) {
            predicateModule = cb.isTrue(pathModule);
        } else {
            predicateModule = cb.isFalse(pathModule);
        }

        query.where(predicateModule, filterCondition);

        int result = entityManager.createQuery(query).getResultList().size();
        logger.log(Level.INFO, "result: {0}", result);
        return result;
    }

    public List<Product> findProductInModule(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters, boolean ifModuleExist) {
//        logger.log(Level.INFO, "findProductInModule called with first : {0} and page size : {1}", new Object[]{first, pageSize});

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);
        query.select(product);

        Path<Boolean> pathModule = product.get(module);

        //sort
        logger.log(Level.INFO, "findProducts called with sort : {0} and sortOrder : {1}", new Object[]{sortField, sortOrder.name()});
        Path<?> path = getPath(sortField, product);
        if (sortOrder == null) {
            //just don't sort
        } else if (sortOrder.equals(SortOrder.ASCENDING)) {
            query.orderBy(cb.asc(path));
        } else if (sortOrder.equals(SortOrder.DESCENDING)) {
            query.orderBy(cb.desc(path));
        } else if (sortOrder.equals(SortOrder.UNSORTED)) {
            //just don't sort
        } else {
            //just don't sort
        }

        //filter
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, Object> filter : filters.entrySet()) {

            logger.log(Level.INFO, "filterCondition with key : {0} and value : {1}", new Object[]{filter.getKey(), filter.getValue()});

            if (!filter.getValue().equals("")) {
                //try as string using like
                Path<String> pathFilter = getStringPath(filter.getKey(), product);
                if (pathFilter != null) {
                    filterCondition = cb.and(filterCondition, cb.like(pathFilter, "%" + filter.getValue() + "%"));
                } else {
                    //try as non-string using equal
                    Path<?> pathFilterNonString = getPath(filter.getKey(), product);
                    filterCondition = cb.and(filterCondition, cb.equal(pathFilterNonString, filter.getValue()));
                }
            }
        }

        Predicate predicateModule;
        if (ifModuleExist) {
            predicateModule = cb.isTrue(pathModule);
        } else {
            predicateModule = cb.isFalse(pathModule);
        }

        query.where(predicateModule, filterCondition);

        TypedQuery<Product> tq = entityManager.createQuery(query);
        
        //pagination
        if (pageSize >= 0) {
            tq.setMaxResults(pageSize);
        }
        if (first >= 0) {
            tq.setFirstResult(first);
        }

        return tq.getResultList();
    }

    private Path<?> getPath(String field, Root<Product> product) {

        //sort
        Path<?> path = null;

        if (field == null) {
            path = product.get("name_en");
        } else {

            switch (field) {
                case "id":
                    path = product.get("id");
                    break;
                case "name_en":
                    path = product.get("name_en");
                    break;
                case "name_fr":
                    path = product.get("name_fr");
                    break;
                case "code":
                    path = product.get("code");
                    break;
                case "cas":
                    path = product.get("cas");
                    break;
                case "einecs":
                    path = product.get("einecs");
                    break;
            }
        }

        return path;
    }

    private Path<String> getStringPath(String field, Root<Product> product) {

        //sort
        Path<String> path = null;

        if (field == null) {
            path = product.get("name_en");
        } else {

            switch (field) {
                case "id":
                    path = product.get("id");
                    break;
                case "name_en":
                    path = product.get("name_en");
                    break;
                case "name_fr":
                    path = product.get("name_fr");
                    break;
                case "code":
                    path = product.get("code");
                    break;
                case "cas":
                    path = product.get("cas");
                    break;
                case "einecs":
                    path = product.get("einecs");
                    break;
            }
        }

        return path;
    }

}
