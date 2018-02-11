package com.lightconf.admin.service.impl;

import com.lightconf.admin.service.ConfService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wuhf
 * @date 2018/02/11
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class ConfServiceImpl implements ConfService {
}
