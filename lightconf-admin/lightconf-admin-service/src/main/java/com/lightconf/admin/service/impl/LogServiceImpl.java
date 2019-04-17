package com.lightconf.admin.service.impl;

import com.lightconf.admin.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class LogServiceImpl implements LogService {

}
