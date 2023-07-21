/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.foo;

import org.bar.B;

import org.mule.api.annotation.NoInstantiate;

@NoInstantiate
public class A
{

    protected B b = null;
}
