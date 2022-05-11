const createMap = ()=>{
    const stocksMap = new Map();
    stocksMap.set('HardSteel PLC', 0);
    stocksMap.set('ShortCircuit PLC', 0);
    stocksMap.set('DryOil PLC', 0);
    return stocksMap

}

const player = {
    playerIndex : 0,
    money : 1000000,
    position: 0,
    stocks: createMap()
}

console.log(player)

