/*
  Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.

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

package com.mysql.cj.x;

import java.util.Properties;

import com.mysql.cj.api.x.NodeSession;
import com.mysql.cj.api.x.XSession;
import com.mysql.cj.api.x.XSessionFactory;
import com.mysql.cj.core.ConnectionString;
import com.mysql.cj.core.exceptions.ExceptionFactory;
import com.mysql.cj.core.exceptions.InvalidConnectionAttributeException;
import com.mysql.cj.mysqlx.devapi.NodeSessionImpl;
import com.mysql.cj.mysqlx.devapi.SessionImpl;

/**
 * Entry point for creating sessions to a MySQL X server.
 *
 */
public class MysqlxSessionFactory implements XSessionFactory {

    private Properties parseUrl(String url) {
        ConnectionString conStr = new ConnectionString(url, null);
        Properties properties = conStr.getProperties();

        if (properties == null) {
            throw ExceptionFactory.createException(InvalidConnectionAttributeException.class, "Initialization via URL failed for \"" + url + "\"");
        }

        return properties;
    }

    @Override
    public XSession getSession(String url) {
        return new SessionImpl(parseUrl(url));
    }

    @Override
    public XSession getSession(Properties properties) {
        return new SessionImpl(properties);
    }

    @Override
    public NodeSession getNodeSession(String url) {
        return new NodeSessionImpl(parseUrl(url));
    }

    @Override
    public NodeSession getNodeSession(Properties properties) {
        return new NodeSessionImpl(properties);
    }

}
