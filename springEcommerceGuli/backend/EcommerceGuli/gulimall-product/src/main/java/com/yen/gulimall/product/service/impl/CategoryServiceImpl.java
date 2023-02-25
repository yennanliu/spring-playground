package com.yen.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yen.gulimall.common.utils.PageUtils;
import com.yen.gulimall.common.utils.Query;
import com.yen.gulimall.product.dao.CategoryDao;
import com.yen.gulimall.product.entity.CategoryEntity;
import com.yen.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// https://youtu.be/5aWkhC7plsc?t=283

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    // since CategoryDao extends from BaseMapper, so we can use BaseMapper below instead for querying data
    // (public interface CategoryDao extends BaseMapper<CategoryEntity>)
    //@Autowired
    //CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     *  method :
     *   - query all categories and their sub categories, then return as tree structure
     */
    @Override
    public List<CategoryEntity> listWithTree() {

        // step 1) get all categories
        List<CategoryEntity> entities =  baseMapper.selectList(null); //NOTE : no query condition (selectList(null)) -> query all data
        System.out.println(">>> entities = " + entities);

        // step 2) wrap as parent-child tree structure
        // step 2-1) get all layer 1 categories
        List<CategoryEntity> levelOneEntities = entities.stream()
                // ParentCid == 0 : layer 1 categories
                .filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == 0;
                }).map(menu -> {
                    // call private method below
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    return menu1.getSort() - menu2.getSort();
                }).collect(Collectors.toList());

        return levelOneEntities;
    }

    // local helper method
    // recursive get all Children from current CategoryEntity : https://youtu.be/5aWkhC7plsc?t=1010
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
                    return categoryEntity.getParentCid() == root.getCatId();
                }).map(categoryEntity -> {
                    // NOTE!!! we call getChildren again below (recursive)
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                })
                .sorted((menu1, menu2) -> {
                    // handle if menu is null (avoid null pointer error)
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                })
                .collect(Collectors.toList());

        return children;
    }

}