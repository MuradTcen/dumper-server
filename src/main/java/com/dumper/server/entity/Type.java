package com.dumper.server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Type {
    L('L'),
    I('I'),
    D('D');

    private final char name;
}
