package com.lightconf.admin.service.impl;

import com.lightconf.admin.service.LogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
public class LogServiceImpl implements LogService {
}
