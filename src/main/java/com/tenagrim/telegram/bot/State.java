package com.tenagrim.telegram.bot;


public enum State {
    START(0L),
    AUTHORIZATION(1L),
    AUTHORIZED(2L),
    FINISH(3L);

    private Long id;

    State(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
