/*
  Copyright (c) 2015, 2016, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package testsuite.mysqlx.devapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.cj.core.exceptions.WrongArgumentException;

public class BindTest extends CollectionTest {
    @Before
    @Override
    public void setupCollectionTest() {
        super.setupCollectionTest();
    }

    @After
    @Override
    public void teardownCollectionTest() {
        super.teardownCollectionTest();
    }

    @Test
    public void removeWithBind() {
        this.collection.add("{\"x\":1}").execute();
        this.collection.add("{\"x\":2}").execute();
        this.collection.add("{\"x\":3}").execute();

        assertEquals(3, this.collection.count());

        assertTrue(this.collection.find("x = 3").execute().hasNext());
        this.collection.remove("x = ?").bind(new Object[] { 3 }).execute();
        assertEquals(2, this.collection.count());
        assertFalse(this.collection.find("x = 3").execute().hasNext());
    }

    @Test
    public void removeWithNamedBinds() {
        this.collection.add("{\"x\":1}").execute();
        this.collection.add("{\"x\":2}").execute();
        this.collection.add("{\"x\":3}").execute();

        assertEquals(3, this.collection.count());

        assertTrue(this.collection.find("x = ?").bind(new Object[] { 3 }).execute().hasNext());
        Map<String, Object> params = new HashMap<>();
        params.put("thePlaceholder", 3);
        this.collection.remove("x = :thePlaceholder").bind(params).execute();
        assertEquals(2, this.collection.count());
        assertFalse(this.collection.find("x = 3").execute().hasNext());
    }

    @Test
    public void bug21798850() {
        Map<String, Object> params = new HashMap<>();
        params.put("thePlaceholder1", 1);
        params.put("thePlaceholder2", 2);
        params.put("thePlaceholder3", 3);
        String q = "$.F1 =:thePlaceholder1 or $.F1 =:thePlaceholder2 or $.F1 =:thePlaceholder3";
        this.collection.find(q).fields("$._id as _id, $.F1 as f1").bind(params).orderBy("$.F1 asc").execute();
    }

    @Test
    public void properExceptionUnboundParams() {
        try {
            this.collection.find("a = :arg1 or b = :arg2").bind("arg1", 1).execute();
            fail("Should raise an exception on unbound placeholder arguments");
        } catch (WrongArgumentException ex) {
            assertEquals("Placeholder 'arg2' is not bound", ex.getMessage());
        }
    }

    @Test
    public void bindArgsOrder() {
        this.collection.add("{'x':1,'y':2}".replaceAll("'", "\"")).execute();
        // same order as query
        assertEquals(1, this.collection.find("x = :x and y = :y").bind("x", 1).bind("y", 2).execute().count());
        // opposite order as query
        assertEquals(1, this.collection.find("x = :x and y = :y").bind("y", 2).bind("x", 1).execute().count());
    }

    // TODO: more tests with unnamed (x = ?) and different bind value types
    // TODO: more tests find & modify
}
