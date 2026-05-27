package com.fleet.fleet_api.services;

import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FleetLogService {

    private final FleetLogRepository fleetLogRepository;

    public List<FleetLog> findAllLogs(){
        return fleetLogRepository.findAllByOrderByTimestamp();
    }

}
