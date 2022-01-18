# 服务器估算

输入流量：<input type="number" id="traffic" value="1"></input>
<select id="trafficType">
    <option value="Gbps">Gbps</option>
    <option value="wEps">wEps</option>
</select>
<button onclick="myEvaluate()">估算</button>
<p id="evaluateResult"></p>
<script>

    function getEps(traffic) {
        return traffic * 1000 * 1000 / 8 * 6 / 100;
    }
    
    function getCapability(machines) {
        if (machines == 1) {
                return 20000;
        } else if(machines == 2) {
            return 0;
        } else if(machines == 3) {
            return 60000;
        } else if (machines <= 30) {
            return (machines * 80000 + 300000) / 9;
        } else if(machines > 30) {
            return machines * 10000;
        }
        return 0;
    }

    function evaluateMachinesByWeps(weps) {
        return evaluateMachines(weps * 10000);
    }
    
    function evaluateMachines(eps) {
        var machines = 1;
        while (getCapability(machines) < eps) {
            machines ++;
        }
        return machines;
    }
    
    function myEvaluate(){
        var traffic = document.getElementById("traffic").value;
        var trafficType = document.getElementById("trafficType").value;
        var eps = traffic * 10000;
        if (trafficType == "Gbps") {
            eps = getEps(traffic);
        }
        var machines = evaluateMachines(eps);
        document.getElementById("evaluateResult").innerHTML="需要标准A58服务器<strong>" + machines + "</strong>台";
    }
</script>
