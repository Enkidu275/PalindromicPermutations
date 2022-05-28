import kotlin.collections.MutableList
fun main(args: Array<String>)
{
    superpermutation(7, true)
}
fun superpermutation(values: Int, info: Boolean): MutableList<Int>
{
    if(values<2)
    {
        println(mutableListOf<Int>(1))
        return mutableListOf<Int>(1)
    }
    if(values==2)
    {
        println(mutableListOf(1,2,1))
        return mutableListOf(1,2,1)
    }
    var a: MutableList<Int> = constructfirstpermutation(values)
    var b: MutableList<MutableList<Int>> = uniquepermutations(a)
    var c: MutableList<Int> = firsthalf(b)
    var d: MutableList<Int> = palindrome(c)
    println("Result: $d")
    if(info)
    {
        var lower: Int = factorial(values) + factorial(values - 1) + factorial (values - 2) + values - 3
        var leng: Int = d.size
        println("The minimum length is at least $lower. The generated result is $leng.")
    }
    return d
}

private fun uniquepermutations(x: MutableList<Int>, y: MutableList<MutableList<Int>> = mutableListOf()): MutableList<MutableList<Int>>
{
    //includes all permutations with cycled copies excluded (ABCD -> DABC -> CDAB -> BCDA -> ABCD)
    //mirrorored iterations still need to be removed with the "trimpermutations" function
    //if input values contain copies, there will unfortunately be duplicate results
    val numberofpermutations: Int = factorial(x.size-1)
    if(y.size==0)//start
    {
        var ik = 1
        while(ik<=numberofpermutations)
        {
            y.add(mutableListOf())
            ik++
        }
    }
    var currentpermutation: Int = 0
    var patterncount: Int
    var patterniterator: Int = 0
    var currentdigit: Int = 0
    while(currentdigit<x.size)
    {
        patterncount = factorial(x.size - currentdigit -1)
        while(currentpermutation<numberofpermutations)
        {
            patterniterator = (currentpermutation/patterncount)%(x.size-currentdigit)
            if(subtractvector(y[currentpermutation],x).size>0)
            {
                y[currentpermutation].add(subtractvector(y[currentpermutation],x)[patterniterator])
            }
            currentpermutation++
        }
        currentpermutation = 0
        currentdigit++
    }
    removepermutationcopies(y)
    return y
}
private fun removepermutationcopies(x: MutableList<MutableList<Int>>)
{
    var i: Int = 0
    var j: Int
    while(i<x.size)
    {
        j=i+1
        while(j<x.size&&i<x.size)
        {
            if(areequal(x[i],x[j])||areequal(x[i],mirrorpermutation(x[j])))
            {
                x.removeAt(j)
            }
            else
            {
                j++
            }
        }
        i++
    }
}
fun factorial(x: Int): Int
{
    var i = 1
    var j = x
    if(x<2)
    {
        return 1
    }
    else
    {
        while(j>1)
        {
            i=i*j
            j--
        }
        return i
    }
}
private fun constructfirstpermutation(x: Int): MutableList<Int>
{
    var y: MutableList<Int> = mutableListOf()
    var i: Int = 1
    if (x<1)
    {
        y.add(1)
        return y
    }
    else
    {
        while(i<=x)
        {
            y.add(i)
            i++
        }
        return y
    }
}
private fun subtractvector(x: MutableList<Int>, y: MutableList<Int>): MutableList<Int>
{
    var z: MutableList<Int> = mutableListOf()
    for(element: Int in y)
        z.add(element)
    for(element: Int in x)
    {
        if(z.contains(element))
        {
            z.removeAt(findfirst(element,z))
        }
    }
    return z
}
private fun cyclepermutation(x: MutableList<Int>): MutableList<Int>
{
    if(x.size==0)
    {
        return x
    }
    else
    {
        var y: MutableList<Int> = mutableListOf(x.last())
        var i = 0
        while(i<x.lastIndex)
        {
            y.add(x.get(i))
            i++
        }
        return y
    }
}
private fun findfirst(x: Int, y: MutableList<Int>): Int
{
    var i: Int = 0
    while(i<y.size)
    {
        if(y.get(i)==x)
        {
            return i
        }
        i++
    }
    return i
}
private fun mirrorpermutation(x: MutableList<Int>): MutableList<Int>
{
    if(x.size<1)
        return mutableListOf(1)
    var y: MutableList<Int> = mutableListOf(x[0])
    var i: Int = x.size-1
    while(i>0)
    {
        y.add(x[i])
        i--
    }
    return y
}
private fun areequal(x: MutableList<Int>, y: MutableList<Int>): Boolean
{
    if(x.size==y.size&&x.size>0)
    {
        var i: Int = x.size - 1
        while(i>0)
        {
            if(x[i]!=y[i])
            {
                return false
            }
            i--
        }
        return true
    }
    else
        return false
}
private fun firsthalf(x: MutableList<MutableList<Int>>): MutableList<Int>
{
    var superperm: MutableList<Int> = conjoinedcycles(x[0])
    val permutationlength: Int = x[0].size
    x.removeAt(0)
    while(x.size>0)
    {
        var overlap: Int = permutationlength - 2
        var match: Boolean = false
        var nextpermutationused: Int = 0
        var maxoverlap: Int = 1
        var mirrored: Boolean = false
        while(overlap>0&&!match)
        {
            var i: Int = 0
            while(i<x.size&&!match)
            {
                if(overlap(superperm,x[i],overlap))
                {
                    nextpermutationused = i
                    mirrored = false
                    maxoverlap = overlap
                    match = true
                }
                if(overlap(superperm,mirrorpermutation(x[i]),overlap))
                {
                    nextpermutationused = i
                    mirrored = true
                    maxoverlap = overlap
                    match = true
                }
                i++
            }
            overlap--
        }
        var nextcycle: MutableList<Int>
        if(!mirrored)
        {
            nextcycle = conjoinedcycles(findcycle(x[nextpermutationused],superperm[superperm.size-maxoverlap]))
        }
        else
        {
            nextcycle = conjoinedcycles(findcycle(mirrorpermutation(x[nextpermutationused]),superperm[superperm.size-maxoverlap]))
        }
        x.removeAt(nextpermutationused)
        while(maxoverlap>0)
        {
            superperm.removeAt(superperm.size-1)
            maxoverlap--
        }
        for(b: Int in nextcycle)
        {
            superperm.add(b)
        }
    }
    return superperm
}
private fun overlap(x: MutableList<Int>, y: MutableList<Int>, z: Int): Boolean
{
    var i: Int = 0
    var relevantcycle: MutableList<Int> = findcycle(y,x[x.size-z])
    while(i<z)
    {
        if(x[x.size-z+i]!=relevantcycle[i])
            return false
        i++
    }
    return true
}
private fun allpermutationcycles(x: MutableList<Int>): MutableList<MutableList<Int>>
{
    val numberofcycles: Int = x.size
    var result: MutableList<MutableList<Int>> = mutableListOf(x)
    var i: Int = 1
    while(i<numberofcycles)
    {
        result.add(cyclepermutation(result.last()))
        i++
    }
    var y: MutableList<Int> = cyclepermutation(x)
    return result
}
private fun findcycle(x: MutableList<Int>, y: Int): MutableList<Int>
{
    var z: MutableList<MutableList<Int>> = allpermutationcycles(x)
    for(cycle: MutableList<Int> in z)
    {
        if(cycle[0]==y)
        {
            return cycle
        }
    }
    return z[0]
}
private fun conjoinedcycles(x: MutableList<Int>): MutableList<Int>
{
    var i: Int = 0
    var z: MutableList<Int> = mutableListOf()
    while(i<x.size)
    {
        z.add(x[i])
        i++
    }
    i = 0
    while(i<x.size-1)
    {
        z.add(x[i])
        i++
    }
    return z
}
private fun palindrome(x: MutableList<Int>): MutableList<Int>
{
    var y: MutableList<Int> = mutableListOf()
    var i = 0
    while(i<x.size)
    {
        y.add(x[i])
        i++
    }
    i = x.size - 2
    while(i>=0)
    {
        y.add(x[i])
        i--
    }
    return y
}
private fun alternatefirsthalf(x: MutableList<MutableList<Int>>): MutableList<Int>//performed worse than original on n=8
{
    var superperm: MutableList<Int> = conjoinedcycles(x[0])
    val permutationlength: Int = x[0].size
    x.removeAt(0)
    while(x.size>0)
    {
        var overlap: Int = permutationlength - 2
        var match: Boolean = false
        var nextpermutationused: Int = 0
        var maxoverlap: Int = 1
        var mirrored: Boolean = false
        while(overlap>0&&!match)
        {
            var i: Int = x.size - 1
            while(i>0&&!match)
            {
                if(overlap(superperm,x[i],overlap))
                {
                    nextpermutationused = i
                    mirrored = false
                    maxoverlap = overlap
                    match = true
                }
                if(overlap(superperm,mirrorpermutation(x[i]),overlap))
                {
                    nextpermutationused = i
                    mirrored = true
                    maxoverlap = overlap
                    match = true
                }
                i--
            }
            overlap--
        }
        var nextcycle: MutableList<Int>
        if(!mirrored)
        {
            nextcycle = conjoinedcycles(findcycle(x[nextpermutationused],superperm[superperm.size-maxoverlap]))
        }
        else
        {
            nextcycle = conjoinedcycles(findcycle(mirrorpermutation(x[nextpermutationused]),superperm[superperm.size-maxoverlap]))
        }
        x.removeAt(nextpermutationused)
        while(maxoverlap>0)
        {
            superperm.removeAt(superperm.size-1)
            maxoverlap--
        }
        for(b: Int in nextcycle)
        {
            superperm.add(b)
        }
    }
    return superperm
}
