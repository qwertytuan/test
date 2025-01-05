package com.example.demo.Service;

import com.example.demo.Model.FriendModel;
import com.example.demo.Model.FriendResponse;
import com.example.demo.Repo.FriendRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendServices {

    @Autowired
    private FriendRepo friendRepo;

    public List<FriendModel> findFriendsByUserId(Long userId) {
        return friendRepo.findAllByUserId(userId);
    }

    public FriendResponse convertToResponse(FriendModel friendModel) {
        return new FriendResponse(
                friendModel.getId(),
                friendModel.getUser().getId(),
                friendModel.getUser().getUsername(),
                friendModel.getFriend().getId(),
                friendModel.getFriend().getUsername(),
                friendModel.getStatus()
        );
    }

    public List<FriendResponse> convertToResponseList(List<FriendModel> friendModels) {
        return friendModels.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}