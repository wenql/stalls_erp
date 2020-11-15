package com.github.wenql.stallserp.modules.sys.service.impl;

import com.github.wenql.stallserp.modules.sys.model.SysMenu;
import com.github.wenql.stallserp.modules.sys.dto.SysMenuNode;
import com.github.wenql.stallserp.modules.sys.mapper.SysMenuMapper;
import com.github.wenql.stallserp.modules.sys.service.SysMenuService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台菜单表 服务实现类
 * </p>
 *
 * @author wenql
 * @since 2020-11-15
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public boolean create(SysMenu SysMenu) {
        SysMenu.setCreateTime(new Date());
        updateLevel(SysMenu);
        return save(SysMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(SysMenu SysMenu) {
        if (SysMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            SysMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            SysMenu parentMenu = getById(SysMenu.getParentId());
            if (parentMenu != null) {
                SysMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                SysMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, SysMenu SysMenu) {
        SysMenu.setId(id);
        updateLevel(SysMenu);
        return updateById(SysMenu);
    }

    @Override
    public Page<SysMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<SysMenu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysMenu::getParentId,parentId)
                .orderByDesc(SysMenu::getSort);
        return page(page,wrapper);
    }

    @Override
    public List<SysMenuNode> treeList() {
        List<SysMenu> menuList = list();
        List<SysMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return result;
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        SysMenu SysMenu = new SysMenu();
        SysMenu.setId(id);
        SysMenu.setHidden(hidden);
        return updateById(SysMenu);
    }

    /**
     * 将SysMenu转化为SysMenuNode并设置children属性
     */
    private SysMenuNode covertMenuNode(SysMenu menu, List<SysMenu> menuList) {
        SysMenuNode node = new SysMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<SysMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

}
