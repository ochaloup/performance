/*
 * Copyright 2014, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.arjuna.ats.jta.xa.performance;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import com.arjuna.ats.internal.arjuna.objectstore.ShadowNoFileLockStore;

import org.junit.BeforeClass;
import org.openjdk.jmh.annotations.*;

import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.*;

/*
 config priority order is:
 1) Runner options
 2) method level annotations
 3) class level annotations
 */
//@Warmup(iterations = JMHConfigJTA.WI, time = JMHConfigJTA.WT)//, timeUnit = JMHConfigJTA.WTU)
//@Measurement(iterations = JMHConfigJTA.MI, time = JMHConfigJTA.MT)//, timeUnit = JMHConfigJTA.MTU)
//@Fork(JMHConfigJTA.BF)
//@Threads(JMHConfigJTA.BT)
@State(Scope.Benchmark)
public class JTAStoreTests extends JTAStoreBase {
    @Setup(Level.Trial)
    @BeforeClass
    public static void setup() throws CoreEnvironmentBeanException {
        JTAStoreBase.setup(ShadowNoFileLockStore.class.getName());
    }

    @Benchmark
    public void jtaTest(Blackhole bh) {
        bh.consume(super.jtaTest());
    }

    public static void main(String[] args) throws RunnerException, CommandLineOptionException, CoreEnvironmentBeanException {
        JMHConfigJTA.runJTABenchmark(JTAStoreTests.class.getSimpleName(), args);
    }
}
