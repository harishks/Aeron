/*
 * Copyright 2014 - 2016 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.aeron.command;

import org.agrona.MutableDirectBuffer;

import static org.agrona.BitUtil.SIZE_OF_INT;
import static org.agrona.BitUtil.SIZE_OF_LONG;

/**
 * Message to denote that new buffers have been setup for a publication.
 *
 * @see ControlProtocolEvents
 *
 * 0                   1                   2                   3
 * 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                         Correlation ID                        |
 * |                                                               |
 * +---------------------------------------------------------------+
 * |                          Session ID                           |
 * +---------------------------------------------------------------+
 * |                           Stream ID                           |
 * +---------------------------------------------------------------+
 * |                    Publication Limit Offset                   |
 * +---------------------------------------------------------------+
 * |                        Log File Length                        |
 * +---------------------------------------------------------------+
 * |                     Log File Name (ASCII)                    ...
 *...                                                              |
 * +---------------------------------------------------------------+
 */
public class PublicationBuffersReadyFlyweight
{
    private static final int CORRELATION_ID_OFFSET = 0;
    private static final int SESSION_ID_OFFSET = CORRELATION_ID_OFFSET + SIZE_OF_LONG;
    private static final int STREAM_ID_FIELD_OFFSET = SESSION_ID_OFFSET + SIZE_OF_INT;
    private static final int PUBLICATION_LIMIT_COUNTER_ID_OFFSET = STREAM_ID_FIELD_OFFSET + SIZE_OF_INT;
    private static final int LOGFILE_FIELD_OFFSET = PUBLICATION_LIMIT_COUNTER_ID_OFFSET + SIZE_OF_INT;

    private MutableDirectBuffer buffer;
    private int offset;

    /**
     * Wrap the buffer at a given offset for updates.
     *
     * @param buffer to wrap
     * @param offset at which the message begins.
     * @return for fluent API
     */
    public final PublicationBuffersReadyFlyweight wrap(final MutableDirectBuffer buffer, final int offset)
    {
        this.buffer = buffer;
        this.offset = offset;

        return this;
    }

    /**
     * return correlation id field
     *
     * @return correlation id field
     */
    public long correlationId()
    {
        return buffer.getLong(offset + CORRELATION_ID_OFFSET);
    }

    /**
     * set correlation id field
     *
     * @param correlationId field value
     * @return flyweight
     */
    public PublicationBuffersReadyFlyweight correlationId(final long correlationId)
    {
        buffer.putLong(offset + CORRELATION_ID_OFFSET, correlationId);

        return this;
    }

    /**
     * return session id field
     *
     * @return session id field
     */
    public int sessionId()
    {
        return buffer.getInt(offset + SESSION_ID_OFFSET);
    }

    /**
     * set session id field
     *
     * @param sessionId field value
     * @return flyweight
     */
    public PublicationBuffersReadyFlyweight sessionId(final int sessionId)
    {
        buffer.putInt(offset + SESSION_ID_OFFSET, sessionId);

        return this;
    }

    /**
     * return stream id field
     *
     * @return stream id field
     */
    public int streamId()
    {
        return buffer.getInt(offset + STREAM_ID_FIELD_OFFSET);
    }

    /**
     * set stream id field
     *
     * @param streamId field value
     * @return flyweight
     */
    public PublicationBuffersReadyFlyweight streamId(final int streamId)
    {
        buffer.putInt(offset + STREAM_ID_FIELD_OFFSET, streamId);

        return this;
    }

    /**
     * The publication limit counter id.
     *
     * @return publication limit counter id.
     */
    public int publicationLimitCounterId()
    {
        return buffer.getInt(offset + PUBLICATION_LIMIT_COUNTER_ID_OFFSET);
    }

    /**
     * set position counter id field
     *
     * @param positionCounterId field value
     * @return flyweight
     */
    public PublicationBuffersReadyFlyweight publicationLimitCounterId(final int positionCounterId)
    {
        buffer.putInt(offset + PUBLICATION_LIMIT_COUNTER_ID_OFFSET, positionCounterId);

        return this;
    }

    /**
     * Get the log file name in ASCII.
     *
     * @return the log file name in ASCII.
     */
    public String logFileName()
    {
        return buffer.getStringAscii(offset + LOGFILE_FIELD_OFFSET);
    }

    /**
     * Set the log file name in ASCII.
     *
     * @param logFileName for the publication buffers.
     * @return the log file name in ASCII.
     */
    public PublicationBuffersReadyFlyweight logFileName(final String logFileName)
    {
        buffer.putStringAscii(offset + LOGFILE_FIELD_OFFSET, logFileName);
        return this;
    }

    /**
     * Get the length of the current message
     *
     * NB: must be called after the data is written in order to be accurate.
     *
     * @return the length of the current message
     */
    public int length()
    {
        return buffer.getInt(offset + LOGFILE_FIELD_OFFSET) + LOGFILE_FIELD_OFFSET + SIZE_OF_INT;
    }
}
