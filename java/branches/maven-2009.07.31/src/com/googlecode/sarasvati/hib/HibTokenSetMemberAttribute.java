/*
    This file is part of Sarasvati.

    Sarasvati is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Sarasvati is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Sarasvati.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2008 Paul Lorenz
*/
package com.googlecode.sarasvati.hib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table (name="wf_token_set_member_attr")
public class HibTokenSetMemberAttribute
{
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  protected Long id;

  @ForeignKey( name="FK_token_set_mbr_attr" )
  @ManyToOne (fetch=FetchType.LAZY, optional=false)
  @JoinColumn (name="token_set_id", nullable=false)
  protected HibTokenSet tokenSet;

  @Column( name="member_index", nullable=true)
  protected Integer memberIndex;

  @Column( name="name", nullable=false)
  protected String name;

  @Column( name="value", nullable=true)
  protected String value;

  protected HibTokenSetMemberAttribute () { /* Default constructor for hibernate */ }

  protected HibTokenSetMemberAttribute (HibTokenSet tokenSet, Integer memberIndex, String name, String value)
  {
   this.tokenSet = tokenSet;
   this.memberIndex = memberIndex;
   this.name = name;
   this.value = value;
  }

  public Long getId ()
  {
    return id;
  }

  public void setId (Long id)
  {
    this.id = id;
  }

  public HibTokenSet getTokenSet()
  {
    return tokenSet;
  }

  public void setTokenSet( HibTokenSet tokenSet )
  {
    this.tokenSet = tokenSet;
  }

  public Integer getMemberIndex()
  {
    return memberIndex;
  }

  public void setMemberIndex( Integer memberIndex )
  {
    this.memberIndex = memberIndex;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  @Override
  public int hashCode ()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( memberIndex == null ) ? 0 : memberIndex.hashCode() );
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    result = prime * result + ( ( tokenSet == null ) ? 0 : tokenSet.hashCode() );
    return result;
  }

  @Override
  public boolean equals (Object obj)
  {
    if ( this == obj ) return true;
    if ( obj == null ) return false;
    if ( !( obj instanceof HibTokenSetMemberAttribute ) ) return false;
    HibTokenSetMemberAttribute other = (HibTokenSetMemberAttribute)obj;
    if ( memberIndex == null )
    {
      if ( other.getMemberIndex() != null ) return false;
    }
    else if ( !memberIndex.equals( other.getMemberIndex() ) ) return false;
    if ( name == null )
    {
      if ( other.getName() != null ) return false;
    }
    else if ( !name.equals( other.getName() ) ) return false;
    if ( tokenSet == null )
    {
      if ( other.tokenSet != null ) return false;
    }
    else if ( !tokenSet.equals( other.getTokenSet() ) ) return false;
    return true;
  }
}