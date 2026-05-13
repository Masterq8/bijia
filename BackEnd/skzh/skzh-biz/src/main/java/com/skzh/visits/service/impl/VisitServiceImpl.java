package com.skzh.visits.service.impl;


import com.skzh.visits.mapper.visitsMapper;
import com.skzh.visits.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
    public class VisitServiceImpl implements VisitService {
        @Autowired
        private visitsMapper visitsMapper;



        @Override
        public List<Map<String, Object>> getDailyVisits() {
            return visitsMapper.getDailyVisits();
        }
    }

