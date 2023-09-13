package com.nuwanw.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Configuration {
    private Clockwork clockwork;
    private Oodo oodo;
    private List<User> users;
}


