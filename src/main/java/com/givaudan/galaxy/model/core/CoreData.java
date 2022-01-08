package com.givaudan.galaxy.model.core;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "core_data")
public class CoreData extends Data implements Serializable {
    
}
