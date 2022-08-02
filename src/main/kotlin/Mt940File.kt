class Mt940File {
    var records: MutableList<Mt940Record?> = ArrayList()
    val entries: List<Mt940Entry?>
        get() {
            val retval: MutableList<Mt940Entry?> = ArrayList()
            for (record in records) {
                retval.addAll(record!!.entries)
            }
            return retval
        }
}