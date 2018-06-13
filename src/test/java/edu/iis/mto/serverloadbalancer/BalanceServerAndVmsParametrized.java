package edu.iis.mto.serverloadbalancer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasLoadPercentageOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.ServerLoadBalancerTest.*;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class BalanceServerAndVmsParametrized {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {4, 6, 1, 4, 2, 75.0d, 66.66d, 0, 1, 0},
                {10, 3, 1, 4, 4, 90.0d, 0.0d, 0, 0, 0},
                {5, 4, 1, 1, 3, 80.0d, 25.0d, 0, 1, 0},
        });
    }

    @Parameterized.Parameter
    public int capacity1;

    @Parameterized.Parameter(1)
    public int capacity2;

    @Parameterized.Parameter(2)
    public int size1;

    @Parameterized.Parameter(3)
    public int size2;

    @Parameterized.Parameter(4)
    public int size3;

    @Parameterized.Parameter(5)
    public double loadPercentage1;

    @Parameterized.Parameter(6)
    public double loadPercentage2;

    @Parameterized.Parameter(7)
    public int indexOfServerForVm1;

    @Parameterized.Parameter(8)
    public int indexOfServerForVm2;

    @Parameterized.Parameter(9)
    public int indexOfServerForVm3;

    @Test
    public void balance_serversAndVms() {
        Server server1 = a(server().withCapacity(capacity1));
        Server server2 = a(server().withCapacity(capacity2));

        Vm vm1 = a(vm().ofSize(size1));
        Vm vm2 = a(vm().ofSize(size2));
        Vm vm3 = a(vm().ofSize(size3));

        Server[] servers = aListOfServersWith(server1, server2);

        balance(servers, aListOfVmsWith(vm1, vm2, vm3));

        assertThat("The server " + indexOfServerForVm1+1 + " should contain the vm 1",
                    servers[indexOfServerForVm1].contains(vm1));
        assertThat("The server " + indexOfServerForVm2+1 + " should contain the vm 2",
                    servers[indexOfServerForVm2].contains(vm2));
        assertThat("The server " + indexOfServerForVm3+1 + " should contain the vm 3",
                    servers[indexOfServerForVm3].contains(vm3));

        assertThat(server1, hasLoadPercentageOf(loadPercentage1));
        assertThat(server2, hasLoadPercentageOf(loadPercentage2));
    }
}
