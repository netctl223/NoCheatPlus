package fr.neatmonster.nocheatplus.utilities;

/**
 * Keep track of frequency of some action, 
 * sort into buckets, representing time intervals, 
 * TODO: find a better name.
 * @author mc_dev
 *
 */
public class ActionFrequency {
	
	/** Reference time for filling in. */
	long time = 0;
	
	final int[] buckets;

	final long durBucket;
	
	public ActionFrequency(final int nBuckets, final long durBucket){
		this.buckets = new int[nBuckets];
		this.durBucket = durBucket;
	}
	
	/**
	 * Update and add.
	 * @param ts
	 */
	public void add(final long now){
		update(now);
	}

	/**
	 * Update without adding, also updates time.
	 * @param now
	 */
	public void update(final long now) {
		final long diff = now - time;
		final int shift = (int) ((float) diff / (float) durBucket);
		if (shift == 0){
			// No update, just fill in.
			buckets[0] ++;
			return; 
		}
		else if (shift >= buckets.length){
			// Clear and fill in (beyond range).
			clear(now);
			buckets[0] ++;
			return;
		}
		// Update buckets.
		for (int i = 0; i < buckets.length - shift; i++){
			buckets[buckets.length - (i + 1)] = buckets[buckets.length - (i + 1 + shift)];
		}
		for (int i = 0; i < shift; i++){
			buckets[i] = 0;
		}
		buckets[0] ++;
		// Set time according to bucket duration (!).
		time += durBucket * shift;
	}

	public void clear(final long now) {
		for (int i = 0; i < buckets.length; i++){
			buckets[i] = 0;
		}
		time = now;
	}
	
	
	/**
	 * Get a weighted sum score, weight for bucket i: w(i) = factor^i. 
	 * @param factor
	 * @return
	 */
	public double getScore(final double factor){
		double res = buckets[0];
		double cf = factor;
		for (int i = 1; i < buckets.length; i++){
			res += cf * buckets[i];
			cf *= factor;
		}
		return res;
	}
	
}
