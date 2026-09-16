package com.pridesys.ticketing.entity;

import java.io.Serializable;

public record ProjectMembershipId(long projectId, long userId) implements Serializable {
}
