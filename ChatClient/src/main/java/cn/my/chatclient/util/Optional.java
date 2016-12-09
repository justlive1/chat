package cn.my.chatclient.util;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Optional<T> {

	private transient CountDownLatch latch;
	private volatile T value;

	private Optional() {
		this.value = null;
	}

	public static <T> Optional<T> empty() {
		return new Optional<>();
	}

	private Optional(T value) {
		this.value = Objects.requireNonNull(value);
	}

	public Optional<T> of(T value) {
		this.value = value;
		return this;
	}

	public Optional<T> of(final CountDownLatch latch) {
		this.latch = latch;
		return this;
	}

	public void clean() {
		this.value = null;
		this.latch.countDown();
		this.latch = null;
	}

	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	public T getWaiting() throws InterruptedException {

		if (latch != null) {
			latch.await();
		}

		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}
	
	public T getWaitingUncheck() {

		if (latch != null) {
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	public T getWaiting(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {

		if (latch != null) {
			boolean reached = latch.await(timeout, unit);
			if (!reached) {
				throw new TimeoutException();
			}
		}

		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
		return value;
	}

	public boolean isPresent() {
		return value != null;
	}

	public boolean isWaitingPresent() {

		return latch != null || value != null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Optional)) {
			return false;
		}

		Optional<?> other = (Optional<?>) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return value != null ? String.format("Optional[%s]", value) : "Optional.empty";
	}
}
