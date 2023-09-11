/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.foo;

import org.bar.B;

import org.mule.api.annotation.NoExtend;

@NoExtend
public class A
{

    protected B b = null;

}
