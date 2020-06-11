
package org.acme;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GreetingRepository extends PagingAndSortingRepository<Greeting, Long> {

    List<Greeting> findByLang(String lang);
}
