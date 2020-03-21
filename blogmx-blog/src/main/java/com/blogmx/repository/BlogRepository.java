package com.blogmx.repository;

import com.blogmx.pojo.SearchBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BlogRepository extends ElasticsearchRepository<SearchBlog, Long> {
}
