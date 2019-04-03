package common.util;

import org.springframework.data.domain.*;
import org.springframework.util.Assert;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class PageUtils {

    /**
     * 分页工具
     * @param contents 待分页数据集。所有参与分页的数据
     * @param pageable 分页配置对象
     * @param <E> 数据类型
     * @return
     */
    public final static <E> Page<E> page(List<E> contents, Pageable pageable) {
        final Sort sort = pageable.getSort();
        Assert.notNull(contents, "totalElements must not be null!");
        Assert.notNull(pageable, "pageable must not be null!");

        final Function<Tuple3<List<E>, Pageable, Integer>, PageImpl<E>> pageBuilder =
                params -> new PageImpl<E>(params.getT1(), params.getT2(), params.getT3());

        final long offset = pageable.getOffset();
        final int size = contents.size();
        final int pageSize = pageable.getPageSize();
        Tuple3<List<E>, Pageable, Integer> param;
        if (offset > size) {
            param = Tuples.of(new ArrayList<>(), pageable, 0);
        } else {
            if (offset <= size && offset + pageSize > size) {
                param = Tuples.of(contents.subList((int) offset, size), pageable, size);
            } else {
                final List<E> eList = contents.subList((int) offset, (int) offset + pageable.getPageSize());
                param = Tuples.of(eList, pageable, size);
            }
        }
        return pageBuilder.apply(param);
    }

    public static void main(String[] args) {
        Integer[] s = {1,2,3,4,5,6,7,8,9};
        Integer[] a = {9,8,7,6,5,4,3,2,1};
        System.err.println(page(Arrays.asList(s), PageRequest.of(0,3)).getContent());
        System.err.println(page(Arrays.asList(a), PageRequest.of(0, 3)).getContent());
    }
}
