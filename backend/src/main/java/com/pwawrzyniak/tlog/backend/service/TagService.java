package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.entity.Tag;
import com.pwawrzyniak.tlog.backend.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

  private static Logger log = LoggerFactory.getLogger(TagService.class);

  @Autowired
  private TagRepository tagRepository;

  @Transactional
  public Tag getOrCreateTagByName(String name) {
    Tag tag = tagRepository.findById(name).orElse(null);
    if (tag == null) {
      tag = tagRepository.saveAndFlush(Tag.builder().name(name).build());
      log.info("Created tag: {}", name);
    }
    return tag;
  }

  @Transactional
  public List<String> findAllTagsSorted() {
    List<String> tags = tagRepository.findAllByOrderByNameAsc().stream().map(Tag::getName).collect(Collectors.toList());
    log.info("Found {} tags", tags.size());
    return tags;
  }
}