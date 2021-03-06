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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.cj.api.x.DatabaseObject.DbObjectStatus;
import com.mysql.cj.api.x.Table;

/**
 * @todo
 */
public class TableTest extends DevApiBaseTestCase {
    @Before
    public void setupTableTest() {
        super.setupTestSession();
    }

    @After
    public void teardownTableTest() {
        super.destroyTestSession();
    }

    @Test
    public void tableBasics() {
        sqlUpdate("drop table if exists tableBasics");
        Table table = this.schema.getTable("tableBasics");
        assertEquals(DbObjectStatus.NOT_EXISTS, table.existsInDatabase());
        sqlUpdate("create table tableBasics (name varchar(32), age int)");
        assertEquals(DbObjectStatus.EXISTS, table.existsInDatabase());
        assertEquals("Table(" + getTestDatabase() + ".tableBasics)", table.toString());
        assertEquals(this.session, table.getSession());
        Table table2 = this.schema.getTable("tableBasics");
        assertFalse(table == table2);
        assertTrue(table.equals(table2));
    }
}
