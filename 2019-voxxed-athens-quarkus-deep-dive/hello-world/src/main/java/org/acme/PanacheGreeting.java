
package org.acme;

import javax.persistence.Entity;
import javax.persistence.Table;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name="greeting")
public class PanacheGreeting extends PanacheEntity {
    String greeting;
    String lang;
}
