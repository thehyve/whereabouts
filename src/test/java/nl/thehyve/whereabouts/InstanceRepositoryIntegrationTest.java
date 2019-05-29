/*
 * Copyright (c) 2019 The Hyve
 * This file is distributed under the MIT License (see accompanying file LICENSE).
 */

package nl.thehyve.whereabouts;

import nl.thehyve.whereabouts.domains.Instance;
import nl.thehyve.whereabouts.repositories.InstanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class InstanceRepositoryIntegrationTest {

    @Autowired
    private InstanceRepository instanceRepository;

    @Test
    public void whenFindById_thenReturnInstance() {
        // given
        Instance instance = instanceRepository.save(new Instance("address1", "source_query_1"));

        // when
        Instance foundInstance = instanceRepository.getOne(instance.getId());

        //then
        assertNotNull(foundInstance);
        assertEquals(instance.getAddress(), foundInstance.getAddress());
        assertEquals(instance.getSourceQuery(), foundInstance.getSourceQuery());
    }

}

