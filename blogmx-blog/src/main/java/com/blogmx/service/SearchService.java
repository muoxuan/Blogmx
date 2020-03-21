package com.blogmx.service;

import com.blogmx.pojo.SearchBlog;
import com.blogmx.repository.BlogRepository;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private BlogRepository blogRepository;

    public List<SearchBlog> getBlogsByKey(String key, int page, int rows) {
        NativeSearchQueryBuilder queryBuilder1 = new NativeSearchQueryBuilder();
        queryBuilder1.withQuery(QueryBuilders.termQuery("titleName", key));
        Page<SearchBlog> search1 = blogRepository.search(queryBuilder1.build());
        List<SearchBlog> list1 = new LinkedList<>();

        NativeSearchQueryBuilder queryBuilder2 = new NativeSearchQueryBuilder();
        queryBuilder2.withQuery(QueryBuilders.termQuery("subTitle", key));
        Page<SearchBlog> search2 = blogRepository.search(queryBuilder2.build());
        List<SearchBlog> list2 = new LinkedList<>();

        for (SearchBlog sb : search1) {
            list1.add(sb);
        }
        for (SearchBlog sb : search2) {
            if (!list1.contains(sb)) {
                list2.add(sb);
            }
        }
        list1.addAll(list2);
        if (rows == -1) {
            return list1;
        }
        page = (page - 1) * rows;
        if (page >= list1.size()) {
            return null;
        }
        return list1.subList(page, Math.min(list1.size(), page + rows));
    }

    public List<SearchBlog> getBlogsByKey(String key, int page) {
        return getBlogsByKey(key, page, 5);
    }
}