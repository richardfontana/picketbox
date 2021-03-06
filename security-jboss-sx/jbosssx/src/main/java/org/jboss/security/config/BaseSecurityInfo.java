/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.security.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.AuthPermission;

/**
 * Base Class of the security info
 * 
 * @author Anil.Saldhana@redhat.com
 * @since Sep 27, 2007
 * @version $Revision$
 * @param <T>
 */
public abstract class BaseSecurityInfo<T>
{
   public static final AuthPermission GET_CONFIG_ENTRY_PERM = new AuthPermission("getLoginConfiguration");

   public static final AuthPermission SET_CONFIG_ENTRY_PERM = new AuthPermission("setLoginConfiguration");

   protected String name;

   protected ArrayList<T> moduleEntries = new ArrayList<T>();
   
   /**
    * Name of the JBoss Module that can be optionally configured for
    * custom login modules etc
    */
   protected Set<String> jbossModuleNames = new HashSet<String>();

   public BaseSecurityInfo()
   {
   }

   public BaseSecurityInfo(String name)
   {
      this.name = name;
   }

   public void add(T ame)
   {
      moduleEntries.add(ame);
   }

   public void add(List<? extends T> moduleEntries)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(SET_CONFIG_ENTRY_PERM);
      this.moduleEntries.addAll(moduleEntries);
   }

   public List<T> getModuleEntries()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GET_CONFIG_ENTRY_PERM);
      return this.moduleEntries;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
	   this.name = name;
   }

   /**
    * Get the name of the JBoss Module
    * @return
    */
   public Set<String> getJBossModuleNames()
   {
	   return jbossModuleNames;
   }

   /**
    * Set the name of the JBoss Module
    * @param jbossModuleName
    */
   public void addJBossModuleName(String jbossModuleName)
   {
       if (jbossModuleName != null && !jbossModuleName.isEmpty())
	       this.jbossModuleNames.add(jbossModuleName);
   }

   protected abstract BaseSecurityInfo<T> create(String name);

   public BaseSecurityInfo<T> merge(BaseSecurityInfo<T> bi)
   {
      if (bi == null)
         return this;
      List<T> al = new ArrayList<T>();
      al.addAll(bi.getModuleEntries());
      al.addAll(this.moduleEntries);
      BaseSecurityInfo<T> mergedBAI = create(name);
      mergedBAI.add(al);
      return mergedBAI;
   }
}