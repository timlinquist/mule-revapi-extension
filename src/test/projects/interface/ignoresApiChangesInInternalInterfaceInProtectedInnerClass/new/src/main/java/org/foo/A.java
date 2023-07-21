/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 */
package org.foo;

import org.bar.B;

public class A
{

    protected class C implements B
    {

        @Override
        public String doStuff() {
            return null;
        }

    }
}
