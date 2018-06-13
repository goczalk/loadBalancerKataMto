package edu.iis.mto.serverloadbalancer;

import static edu.iis.mto.serverloadbalancer.CurrentLoadPercentageMatcher.hasLoadPercentageOf;
import static edu.iis.mto.serverloadbalancer.ServerBuilder.server;
import static edu.iis.mto.serverloadbalancer.VmBuilder.vm;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ServerLoadBalancerParametrizedTest extends ServerLoadBalancerBaseTest{

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
            {1,1,100.0d}, {2,1,50.0d}, {4,1,25.0d}, {100,1,1.0d}
        });
    }

    @Parameterized.Parameter
    public int serverCapacity;

    @Parameterized.Parameter(1)
    public int vmSize;

    @Parameterized.Parameter(2)
    public double loadPercentage;


	@Test
	public void balancingOneServerWithCertainCapacity_andCertainSlotVm_fillsTheServerWithTheVm() {
		Server theServer = a(server().withCapacity(serverCapacity));
		Vm theVm = a(vm().ofSize(vmSize));
		balance(aListOfServersWith(theServer), aListOfVmsWith(theVm));

		assertThat(theServer, hasLoadPercentageOf(loadPercentage));
		assertThat("the server should contain vm", theServer.contains(theVm));
	}
	
	
}
