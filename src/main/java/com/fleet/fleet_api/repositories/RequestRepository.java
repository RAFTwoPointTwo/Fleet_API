package com.fleet.fleet_api.repositories;

import com.fleet.fleet_api.models.Request;
import com.fleet.fleet_api.utilities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request , Long> {

    List<Request> findAllByOrderByCreatedAtDesc();

    List<Request> findByReasonContainsIgnoreCaseOrderByCreatedAtDesc(String reason);

    List<Request> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);

    List<Request> findByRequesterIdAndStatusOrderByCreatedAtDesc(Long requesterId, RequestStatus status);

    List<Request> findByValidatorIdOrderByCreatedAtDesc(Long validatorId);

    List<Request> findByValidatorIdAndStatusOrderByCreatedAtDesc(Long validatorId, RequestStatus status);

    List<Request> findByStatusOrderByCreatedAtDesc(RequestStatus status);

    List<Request> findByAssetIdOrderByCreatedAtDesc(Long assetId);

    List<Request> findByPackIdOrderByCreatedAtDesc(Long packId);

}