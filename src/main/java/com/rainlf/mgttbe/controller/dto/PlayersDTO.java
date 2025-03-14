package com.rainlf.mgttbe.controller.dto;


import lombok.Data;

import java.util.List;

@Data
public class PlayersDTO {
    private List<UserDTO> currentPlayers;
    private List<UserDTO> allPlayers;
}
